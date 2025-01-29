pipeline {
    agent any
    environment {
            // 配置全局环境变量
            DOCKER_REGISTRY = 'your-docker-registry.com' // Docker Registry 地址
            DOCKER_CREDENTIALS_ID = 'docker-credentials' // Jenkins 中 Docker 凭据的 ID
            IMAGE_NAME = 'soloscan' // 镜像名称，与项目名称一致
            BRANCH_NAME = 'main' // Git 分支
            GIT_URL = 'https://github.com/hanzhihua-0725/soloscan.git' // Git 仓库地址
        }

    parameters {
            // 定义一个下拉菜单参数
            choice(
                name: 'RUN_STAGE',
                choices: ['All', 'Checkout Code', 'Build and Test', 'Deploy'], // 下拉选项
                description: '选择要运行的阶段，选择 "All" 运行所有阶段'
            )
        }
        stages {
            stage('Checkout Code') {
                when {
                    expression { params.RUN_STAGE == 'All' || params.RUN_STAGE == 'Checkout Code' }
                }
                steps {
                    echo "Checking out code..."
                    git branch: BRANCH_NAME, url: GIT_URL
                }
            }
            stage('Build and Test') {
                when {
                    expression { params.RUN_STAGE == 'All' || params.RUN_STAGE == 'Build and Test' }
                }
                steps {
                    echo "Building and testing the project..."
                    sh 'mvn clean install'
                }
            }
            stage('Deploy') {
                when {
                    expression { params.RUN_STAGE == 'All' || params.RUN_STAGE == 'Deploy' }
                }
                steps {
                    echo "Deploying the project..."
                }
            }
        }
}