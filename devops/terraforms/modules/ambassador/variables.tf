variable "ambassador_namespace" {
  description = "The namespace in which Ambassador will be deployed."
  type        = string
  default     = "ambassador"
}

variable "ambassador_version" {
  description = "The version of Ambassador."
  type        = string
  default     = "6.7.9"
}
