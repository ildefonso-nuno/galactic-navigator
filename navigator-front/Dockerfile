# Use an official Node runtime as a parent image
FROM --platform=linux/amd64 node:16

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the current directory contents into the container at /usr/src/app
COPY . .

# Install any needed packages specified in package.json
RUN npm install

# Additional dependencies
RUN npm install @mui/material @emotion/react @emotion/styled react-force-graph axios

# Build the React app
RUN npm run build

# Install `serve` to serve the app on port 80
RUN npm install -g serve

# Start the server using `serve`
CMD ["serve", "-s", "build", "-l", "80"]