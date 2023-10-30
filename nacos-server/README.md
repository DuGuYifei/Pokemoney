# README

## How to run nacos-server

Download nacos-server zip from release page.
Link is : [https://github.com/DuGuYifei/Pokemoney/releases/tag/nacos-server](https://github.com/DuGuYifei/Pokemoney/releases/tag/nacos-server)

Unzip and run the following command:

```bash
cd nacos/bin
sh startup.sh -m standalone
```

## Cluster mode
For cluster mode, 
1. create cluster.conf under the directory of `nacos/conf/` 
2. add all cluster's ip:port info, one cluster node per line.

```bash
cd nacos/bin
sh startup.sh
```

## Embedded database

```bash
cd nacos/bin
sh startup.sh -m embedded
```

