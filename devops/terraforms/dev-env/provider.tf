terraform {
  required_version = ">= 0.14.8"

  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = ">= 2.2.0"
    }

    helm = {
      version = "2.1.2"
    }

    github = {
      source  = "integrations/github"
      version = "~> 4.0"
    }

    kubernetes-alpha = {
      version = "0.4.0"
    }

  }
}


locals {
  kube_config_path = "~/.kube/config"
}

provider "kubernetes-alpha" {
  config_path = local.kube_config_path
}

provider "kubernetes" {
  config_path = local.kube_config_path
}

provider "helm" {
  kubernetes {
    config_path = local.kube_config_path
  }
}
provider "kubectl" {
  config_path = local.kube_config_path
}

provider "github" {
  token = var.github_token
}
