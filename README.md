# VMware-Exporter
A working in progress VMware Exporter developed in Spring boot and for vSphere 8

## What is?
This is a VMware exporter developed in Java Spring boot for vSphere 8. In the repository are included the external libraries of JAVA VMware SDK.

At this time the exporter can collect:
 - Datastores freespace
 - Datastores total capacity
 - Host total CPUs
 - Host total memory

The exporter caches all the values, but collection interval can be adjusted/defined. For that, you use the vsphere.properties file to set the username, password, vCenter and the interval for the collection of the metrics.

## Run as container
The exporter can run locally and as a docker container. A docker compose file is also present in this repository, as the Dockerfile to build the docker image.
