locals {
  bucket_name = "galactic-navigator-tf"
  table_name  = "gnTf"

  ecr_backend_repo_name  = "navigator-backend-repo"
  ecr_frontend_repo_name = "navigator-frontend-repo"

  galactic_navigator_cluster_name = "galactic-navigator-cluster"
  availability_zones              = ["eu-central-1a", "eu-central-1b", "eu-central-1c"]
  ecs_task_execution_role_name    = "navigator-task-execution-role"

  navigator_backend_task_family = "navigator-backend-task"
  backend_container_port        = 8080
  navigator_backend_task_name   = "navigator-backend-task"

  navigator_frontend_task_family = "navigator-frontend-task"
  frontend_container_port        = 3001
  navigator_frontend_task_name   = "navigator-frontend-task"

  alb_backend_name               = "navigator-backend-alb"
  backend_target_group_name      = "navigator-backend-alb-tg"
  navigator_backend_service_name = "navigator-backend-service"

  alb_frontend_name               = "navigator-frontend-alb"
  frontend_target_group_name      = "navigator-frontend-alb-tg"
  navigator_frontend_service_name = "navigator-frontend-service"

  navigator_frontend_repo_url = "navigator_frontend_repo_url"
}