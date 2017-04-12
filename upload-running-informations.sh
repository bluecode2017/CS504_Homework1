#!/usr/bin/env bash
curl -H "Content-type: application/json" localhost:8080/bulkUpload  -d @runningInformations.json
