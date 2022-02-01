
def call(){

  pipeline {
        agent any
        environment {
            NEXUS_USER         = credentials('nexus-user')
            NEXUS_PASSWORD     = credentials('nexus-pass')
        }
        parameters {
            choice choices: ['Maven', 'Gradle'], description: 'Seleccione una herramienta para proceder a compilar', name: 'compileTool'
            text description: 'Enviar los stages separados por ";" .... Vacío si necesita todos los stages', name: 'stages'
        }
        stages {
            stage("Pipeline"){
                steps {
                    script{
                    switch(params.compileTool)
                        {
                            case 'Maven':
                                maven.call(params.stages)
                            break;
                            case 'Gradle':
                                gradle.call(params.stages)
                            break;
                        }
                    }
                }
            }
        }
        post{
            success{
                slackSend color: 'good', message: "[Su Nombre] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack'
            }
            failure{
                slackSend color: 'danger', message: "[Su Nombre] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack'
            }
        }
    }

}

return this;