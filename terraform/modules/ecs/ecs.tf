resource "aws_ecs_cluster" "galactic_navigator_cluster" {
  name = var.galactic_navigator_cluster_name
}

resource "aws_default_vpc" "default_vpc" {}

resource "aws_default_subnet" "default_subnet_a" {
  availability_zone = var.availability_zones[0]
}

resource "aws_default_subnet" "default_subnet_b" {
  availability_zone = var.availability_zones[1]
}

resource "aws_default_subnet" "default_subnet_c" {
  availability_zone = var.availability_zones[2]
}

resource "aws_ecs_task_definition" "navigator_backend_task" {
  family                   = var.navigator_backend_task_family
  container_definitions    = <<DEFINITION
  [
    {
      "name": "${var.navigator_backend_task_name}",
      "image": "${var.navigator_backend_repo_url}",
      "essential": true,
      "portMappings": [
        {
          "containerPort": ${var.backend_container_port},
          "hostPort": ${var.backend_container_port}
        }
      ],
      "memory": 512,
      "cpu": 256
    }
  ]
  DEFINITION
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  memory                   = 512
  cpu                      = 256
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
}

/*
resource "aws_ecs_task_definition" "navigator_frontend_task" {
  family                   = var.navigator_frontend_task_family
  container_definitions    = <<DEFINITION
  [
    {
      "name": "${var.navigator_frontend_task_name}",
      "image": "${var.navigator_frontend_repo_url}",
      "essential": true,
      "portMappings": [
        {
          "containerPort": ${var.frontend_container_port},
          "hostPort": ${var.frontend_container_port}
        }
      ],
      "memory": 512,
      "cpu": 256
    }
  ]
  DEFINITION
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  memory                   = 512
  cpu                      = 256
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
}
*/

resource "aws_iam_role" "ecs_task_execution_role" {
  name               = var.ecs_task_execution_role_name
  assume_role_policy = data.aws_iam_policy_document.assume_role_policy.json
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_alb" "alb_backend" {
  name               = var.alb_backend_name
  load_balancer_type = "application"
  subnets = [
    "${aws_default_subnet.default_subnet_a.id}",
    "${aws_default_subnet.default_subnet_b.id}",
    "${aws_default_subnet.default_subnet_c.id}"
  ]
  security_groups = [aws_security_group.load_balancer_security_group.id]
}

/*
resource "aws_alb" "alb_frontend" {
  name               = var.alb_frontend_name
  load_balancer_type = "application"
  subnets = [
    "${aws_default_subnet.default_subnet_a.id}",
    "${aws_default_subnet.default_subnet_b.id}",
    "${aws_default_subnet.default_subnet_c.id}"
  ]
  security_groups = [aws_security_group.load_balancer_security_group.id]
}
*/

resource "aws_security_group" "load_balancer_security_group" {
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}


resource "aws_lb_target_group" "backend_target_group" {
  name        = var.backend_target_group_name
  port        = var.backend_container_port
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = aws_default_vpc.default_vpc.id
}

resource "aws_lb_listener" "backend_listener" {
  load_balancer_arn = aws_alb.alb_backend.arn
  port              = "80"
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.backend_target_group.arn
  }
}

/*
resource "aws_lb_target_group" "frontend_target_group" {
  name        = var.frontend_target_group_name
  port        = var.frontend_container_port
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = aws_default_vpc.default_vpc.id
}

resource "aws_lb_listener" "frontend_listener" {
  load_balancer_arn = aws_alb.alb_frontend.arn
  port              = "80"
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.frontend_target_group.arn
  }
}
*/

resource "aws_ecs_service" "navigator_backend_service" {
  name            = var.navigator_backend_service_name
  cluster         = aws_ecs_cluster.galactic_navigator_cluster.id
  task_definition = aws_ecs_task_definition.navigator_backend_task.arn
  launch_type     = "FARGATE"
  desired_count   = 1

  load_balancer {
    target_group_arn = aws_lb_target_group.backend_target_group.arn
    container_name   = aws_ecs_task_definition.navigator_backend_task.family
    container_port   = var.backend_container_port
  }

  network_configuration {
    subnets          = [aws_default_subnet.default_subnet_a.id, aws_default_subnet.default_subnet_b.id, aws_default_subnet.default_subnet_c.id]
    assign_public_ip = true
    security_groups  = [aws_security_group.service_security_group.id]
  }
}

/*
resource "aws_ecs_service" "navigator_frontend_service" {
  name            = var.navigator_frontend_service_name
  cluster         = aws_ecs_cluster.galactic_navigator_cluster.id
  task_definition = aws_ecs_task_definition.navigator_frontend_task.arn
  launch_type     = "FARGATE"
  desired_count   = 1

  load_balancer {
    target_group_arn = aws_lb_target_group.frontend_target_group.arn
    container_name   = aws_ecs_task_definition.navigator_frontend_task.family
    container_port   = var.frontend_container_port
  }

  network_configuration {
    subnets          = [aws_default_subnet.default_subnet_a.id, aws_default_subnet.default_subnet_b.id, aws_default_subnet.default_subnet_c.id]
    assign_public_ip = true
    security_groups  = [aws_security_group.service_security_group.id]
  }
}
*/

resource "aws_security_group" "service_security_group" {
  ingress {
    from_port       = 0
    to_port         = 0
    protocol        = "-1"
    security_groups = [aws_security_group.load_balancer_security_group.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}