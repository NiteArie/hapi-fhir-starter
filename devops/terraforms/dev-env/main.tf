
module "install_ngrok_operator" {
  source = "../modules/ngrok"
}

module "install_cert_mananger" {
  source = "../modules/cert-manager"
}

module "install_github_actions" {
  source       = "../modules/github-actions"
  github_token = var.github_token
  depends_on = [
    module.install_cert_mananger.id
  ]
}