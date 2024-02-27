variable "galactic_navigator_cluster_name" {
  description = "ECS Cluster name"
  type        = string
}

variable "availability_zones" {
  description = "eu-central-1 AZs"
  type        = list(string)
}

variable "navigator_backend_task_family" {
  description = "ECS Task Family"
  type        = string
}

variable "navigator_frontend_task_family" {
  description = "ECS Task Family"
  type        = string
}

variable "navigator_backend_repo_url" {
  description = "Backend ECR repo url"
  type        = string
}

variable "navigator_frontend_repo_url" {
  description = "Frontend ECR repo url"
  type        = string
}

variable "backend_container_port" {
  description = "Backend Container Port"
  type        = string
}

variable "frontend_container_port" {
  description = "Frontend Container Port"
  type        = string
}

variable "navigator_backend_task_name" {
  description = "Backend ECS task name"
  type        = string
}

variable "navigator_frontend_task_name" {
  description = "Frontend ECS task name"
  type        = string
}

variable "ecs_task_execution_role_name" {
  description = "ECS Task Execution Role Name"
  type        = string
}

variable "alb_backend_name" {
  description = "ALB backend name"
  type        = string
}

variable "backend_target_group_name" {
  description = "Backend Target Group Name"
  type        = string
}

variable "navigator_backend_service_name" {
  description = "Backend Service Name"
  type        = string
}

variable "alb_frontend_name" {
  description = "ALB backend name"
  type        = string
}

variable "frontend_target_group_name" {
  description = "Frontend Target Group Name"
  type        = string
}

variable "navigator_frontend_service_name" {
  description = "Frontend Service Name"
  type        = string
}