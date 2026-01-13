#!/bin/sh
set -e

# Replace HOST_IP placeholder in nginx.conf if HOST_IP env var is set
if [ -n "$HOST_IP" ] && [ "$HOST_IP" != "host.docker.internal" ]; then
    echo "Using HOST_IP: $HOST_IP"
    sed -i "s|host.docker.internal|$HOST_IP|g" /etc/nginx/nginx.conf
else
    echo "Using host.docker.internal (Docker Desktop default)"
fi

# Execute the original nginx entrypoint
exec /docker-entrypoint.sh "$@"

