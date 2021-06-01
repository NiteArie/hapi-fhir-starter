terraform {

  required_version = ">=0.14.0"
  required_providers {
    k3d = {
      source  = "3rein/k3d"
      version = "0.0.4"
    }
  }
}

resource "k3d_cluster" "healthcarelab-cluster" {
  name    = "healthcarelab-cluster"
  servers = 1
  agents  = 1

  kube_api {
    host      = "localhost"
    host_ip   = "127.0.0.1"
    host_port = 6445
  }

  image   = "rancher/k3s:latest"
  network = "healthcare-custom-net"
  token   = "superSecretToken"

  port {
    host_port      = 80
    container_port = 80
    node_filters = [
      "loadbalancer",
    ]
  }

  k3d {
    disable_load_balancer     = false
    disable_image_volume      = false
    disable_host_ip_injection = false
  }

  k3s {
    extra_server_args = [
      "--tls-san=localhost"
    ]
  }

  kubeconfig {
    update_default_kubeconfig = true
    switch_current_context    = true
  }
}
