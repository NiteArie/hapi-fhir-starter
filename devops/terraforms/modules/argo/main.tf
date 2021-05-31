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
# Create namespace to deploy Argo
# ----------------------------------------------------------------------------------------------------------------------
resource "kubernetes_namespace" "argo-namespace" {
  metadata {
    name = var.argo_namespace
  }
}


# ----------------------------------------------------------------------------------------------------------------------
# ArgoCD Resources
# ----------------------------------------------------------------------------------------------------------------------
resource "helm_release" "deploy-argo-cd" {
  name = "argo-cd"

  repository = "https://argoproj.github.io/argo-helm/"
  chart      = "argo-cd"
  version    = var.argo_cd_version

  set {
    name  = "service.type"
    value = "ClusterIP"
  }

  set {
    name  = "installCRDs"
    value = false
  }

  set {
    name  = "namespace"
    value = var.argo_namespace
  }

  depends_on = [
    kubernetes_namespace.argo-namespace
  ]

}
