

class ProjectDocker{
    /*
    Function is for  get docker image path
    */
    static String getDockerImagePath(String name){
        String result

        switch(name){
            case 'jenkins-2.60.2':
                result = 'jenkins:2.60.2'
                break
            case 'jenkins-2.60.3':
                result = 'jenkins:2.60.3'
                break
            default:
                result = 'jenkins:2.60.1'
                break
        }

        return result
    }


    /* Function is for get docker imagewhich containers part of the name. Use:
    - Args:
        name(require): type String: Image part of the name
     */
    static ArrayList getImage(Map Args, def steps){
        String funcName = 'getImage'
        Map argsList = ['name': [value: Args['name'], type: 'string']]
        String command
        String listImages
        ArrayList result = new ArrayList()


        // Checking Arguments
        checkArgs(argsList, funcName, steps)

        command = 'docker image --format "{{.Repository}}:{{.Tag}}:{{.ID}}'
        listImages = steps.sh(script: command, returnStdout: true)

        for(String image in listImages.split('\n')){
            if(image.contains(Args['name'])){
                result.add(image)
            }
        }

        return result
    }

    /* Function is for pull docker image. Use:
    - Args:
        name(require): type String; Docker image name
        tag: type String; Docker image tag; default is 'latest
     */
    static def pullDockerImage(Map Args, def steps){
        String funcName = 'pullDockerImage'
        Map argsList = ['name': [value: Args['name'], type: 'string'],
                        'tag': [value: Args['tag'], type: 'string', require: false]]
    }
    String cred
    String command


    // Checking arguments
    checkArgs(argsList, funcName, steps)
    Args['tag'] = Args.contaisnKey('tag') ? Args['tag'] : 'latest'

    cred = getCredentional(Args['name'])
    steps.withCredentials([step.username.Password(credentialsId: cred, usernameVariable: 'USER_NAME', passwordVariable 'PASSWORD')]){
        command = """docker login --username \$USER_NAME --password \$PASSWORD ${Args['name']}
                     |docker pull ${Args['name']}:${Args['tag']}
                     |docker logout ${Args['name']}""".stripMargin()
        steps.sh(command)
    }

}


















