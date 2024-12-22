
docker service scale lmsusers=0
remove=$(docker service rm lmsusers)

if [[ "$remove" == "lmsusers" ]]; then
  echo "Stopped lmsusers"

    db_base_name="users_db_"
    db_base_port=56000

    latest_i=$(docker ps --filter "name=^${db_base_name}[1-9][0-9]*$" --format "{{.Names}}" | sort -V | tail -n 1 | grep -oE '[0-9]+$')

    for ((i = 1; i <= latest_i; i++)); do
      db_name="$db_base_name${i}"
      db_port=$(($db_base_port + i))

      docker stop ${db_name}
      docker rm ${db_name}

      echo "Stopped ${db_name} on port ${db_port}"
    done
else
  echo "Could not stop lmsusers"
fi