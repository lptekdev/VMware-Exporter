# Java VMware Prometheus Exporter
A working in progress VMware Prometheus Exporter developed in Java Spring boot and for vSphere 8

## What is?
This is a VMware exporter developed in Java Spring boot for vSphere 8. In the repository are included the external libraries of JAVA VMware SDK.

At this time the exporter can collect:
 - Datastores freespace
 - Datastores total capacity
 - Host total CPUs
 - Host total memory

The exporter caches all the values, and collection interval must be adjusted/defined (in seconds). The values are cached because I will also collect vSAN metrics, and from my experience those took some time which can translate in Prometheus scrape timeout and metrics collection failure. 
To configure the vCenter, credentials and collection interval in seconds, you use the vsphere.properties file.

## Run as container
The exporter can run locally and as a docker container. A docker compose file is also present in this repository, as the Dockerfile to build the docker image.
