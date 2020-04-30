export class DeploymentSettings {

    public clusterNodes: Number;
    public type: String;
    public dockerImage: String;
    public kubernetesImage: String;
    public kubernetesPod: String;
    
    constructor() {
        this.type = 'NONE';
        this.clusterNodes = 3;
    }

}