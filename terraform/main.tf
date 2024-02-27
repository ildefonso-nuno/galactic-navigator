terraform {
  required_version = "~> 1.3"

  backend "s3" {
    bucket         = "galactic-navigator-tf"
    key            = "tf-infra/terraform.tfstate"
    region         = "eu-central-1"
    dynamodb_table = "gnTf"
    encrypt        = true
  }

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

module "tf-state" {
  source      = "./modules/tf-state"
  bucket_name = local.bucket_name
  table_name  = local.table_name
}

module "ecrRepo" {
  source = "./modules/ecr"

  ecr_backend_repo_name  = local.ecr_backend_repo_name
  ecr_frontend_repo_name = local.ecr_frontend_repo_name
}

module "ecsCluster" {
  source = "./modules/ecs"

  galactic_navigator_cluster_name = local.galactic_navigator_cluster_name
  availability_zones              = local.availability_zones

  navigator_backend_task_family = local.navigator_backend_task_family
  navigator_backend_repo_url    = module.ecrRepo.navigator_backend_repo_url
  backend_container_port        = local.backend_container_port
  navigator_backend_task_name   = local.navigator_backend_task_name
  ecs_task_execution_role_name  = local.ecs_task_execution_role_name

  navigator_frontend_task_family = local.navigator_frontend_task_family
  navigator_frontend_repo_url    = module.ecrRepo.navigator_frontend_repo_url
  frontend_container_port        = local.frontend_container_port
  navigator_frontend_task_name   = local.navigator_frontend_task_name

  alb_backend_name               = local.alb_backend_name
  backend_target_group_name      = local.backend_target_group_name
  navigator_backend_service_name = local.navigator_backend_service_name

  alb_frontend_name               = local.alb_frontend_name
  frontend_target_group_name      = local.frontend_target_group_name
  navigator_frontend_service_name = local.navigator_frontend_service_name
}