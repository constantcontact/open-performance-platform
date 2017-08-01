#!/bin/bash
RUN bin/elasticsearch-plugin remove x-pack
# exec CMD
echo "---> exec docker CMD"
echo "$@"
exec "$@"