if [[ $1 =~ ^-?[0-9]+$ ]]; then
  # It's a valid integer (including negatives)
  if (( $1 < 1 )); then
    ./shutdown.sh
    exit
  fi
else
  echo "Error: Argument is not a valid number"
  exit
fi

db_base_name="users_db_"
db_base_port=56000

# Check the latest DB instance
latest_i=$(docker ps --filter "name=^${db_base_name}[1-9][0-9]*$" --format "{{.Names}}" | sort -V | tail -n 1 | grep -oE '[0-9]+$')

# Check if we need to remove any DB containers
if (( latest_i > $1 )); then
  for ((i = $1+1; i <= latest_i; i++)); do
    db_name="$db_base_name${i}"
    db_port=$(($db_base_port + i))

    # Check if the container exists and remove it
    if docker ps -a --filter "name=${db_name}" --format "{{.Names}}" | grep -q "^${db_name}$"; then
      docker stop ${db_name}
      docker rm ${db_name}
      echo "Stopped and removed existing container: ${db_name}"
    fi

    echo "Stopped ${db_name} on port ${db_port}"
  done

else
  # Create new DB containers if necessary
  if (( latest_i < $1 )); then
    for ((i = 1; i <= $1; i++)); do
      db_name="$db_base_name${i}"
      db_port=$((db_base_port + i))

      # Check if the container already exists
      if docker ps -a --filter "name=${db_name}" --format "{{.Names}}" | grep -q "^${db_name}$"; then
        docker stop ${db_name}
        docker rm ${db_name}
        echo "Stopped and removed existing container: ${db_name}"
      fi

      docker run -d \
        --name "${db_name}" \
        --network lms_overlay_attachable_network \
        -e POSTGRES_USER=postgres \
        -e POSTGRES_PASSWORD=password \
        -p "${db_port}:5432" \
        postgres

      echo "Started ${db_name} on port ${db_port}"
    done
  fi
fi

# Scaling the lmsusers service
echo "Running $1 instances of lmsusers, each connecting to a different postgres DBMS"

if docker service ls --filter "name=lmsusers" --format "{{.Name}}" | grep -q "^lmsusers$"; then
  docker service scale lmsusers=$1
else
  docker service create -d \
    --name lmsusers \
    --env SPRING_PROFILES_ACTIVE=bootstrap \
    --env spring.datasource.url=jdbc:postgresql://users_db_{{.Task.Slot}}:5432/postgres \
    --env spring.datasource.username=postgres \
    --env spring.datasource.password=password \
    --env file.upload-dir=/home/hugoferreiracoelho86/docker/uploads-psoft-g1-instance{{.Task.Slot}} \
    --env spring.rabbitmq.host=rabbitmq \
    --mount type=bind,source=/home/hugoferreiracoelho86/docker,target=/tmp/uploads-psoft-g1-instance{{.Task.Slot}} \
    --publish 8084:8080 \
    --network lms_overlay_attachable_network \
    hugocoelhoisep/lmsusers:latest

  docker service scale lmsusers=$1
fi
