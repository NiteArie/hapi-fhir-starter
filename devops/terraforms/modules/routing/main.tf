terraform {
  required_version = ">= 0.14.8"

  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = ">= 2.2.0"
    }

    kubernetes-alpha = {
      version = "0.4.0"
    }
  }
}
