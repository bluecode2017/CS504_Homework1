#!/usr/bin/env bash
curl -H "Content-type: application/json" localhost:8080/runninginformations  -d @runningInformations.json
