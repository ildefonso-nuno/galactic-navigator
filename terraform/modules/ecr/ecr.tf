resource "aws_ecr_repository" "navigator-backend-repo" {
  name = var.ecr_backend_repo_name
}

resource "aws_ecr_repository" "navigator-frontend-repo" {
  name = var.ecr_frontend_repo_name
}