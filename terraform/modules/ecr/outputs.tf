output "navigator_backend_repo_url" {
  value = aws_ecr_repository.navigator-backend-repo.repository_url
}

output "navigator_frontend_repo_url" {
  value = aws_ecr_repository.navigator-frontend-repo.repository_url
}