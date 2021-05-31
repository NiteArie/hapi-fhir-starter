terraform {
  required_version = ">= 0.13.3"

  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = ">= 2.2.0"
    }

    helm = {
      version = "2.1.2"
    }
  }

}

# ----------------------------------------------------------------------------------------------------------------------
# Create namespace to deploy Argo Ecosystem
# ----------------------------------------------------------------------------------------------------------------------
resource "kubernetes_namespace" "ambassador-namespace" {
  metadata {
    name = var.ambassador_namespace
  }
}

# ----------------------------------------------------------------------------------------------------------------------
# Ambassardor Resources
# ----------------------------------------------------------------------------------------------------------------------
resource "helm_release" "ambassador-helm" {
  name = "ambassador"

  repository = "https://www.getambassador.io"
  chart      = "ambassador"
  version    = var.ambassador_version
  namespace  = var.ambassador_namespace
  depends_on = [
    kubernetes_namespace.ambassador-namespace
  ]

  set {
    name  = "enableAES"
    value = false
  }
}
