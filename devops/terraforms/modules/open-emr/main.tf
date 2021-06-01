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
resource "helm_release" "deploy-traefik-controller" {
  name    = "traefik"
  chart   = "../../charts/open-emr-charts"
  version = "0.1.0"
  wait    = true
}
