terraform {

  required_version = ">=0.14.0"
  required_providers {
    helm = {
      version = "2.1.2"
    }
  }
}

# ----------------------------------------------------------------------------------------------------------------------
# ArgoCD Resources
# ----------------------------------------------------------------------------------------------------------------------
resource "helm_release" "deploy-nginx-controller" {
  name = "ingress-nginx"

  repository = "https://kubernetes.github.io/ingress-nginx"
  chart      = "ingress-nginx"
  version    = "3.31.0"

  set {
    name  = "hostPort.enabled"
    value = true
  }

  set {
    name  = "defaultBackend.enabled"
    value = true
  }
  wait = true
}
