variable "bucket_name" {
  description = "tf-state S3 bucket name"
  type        = string
}

variable "table_name" {
  description = "tf-state Dynamo DB table name"
  type        = string
}