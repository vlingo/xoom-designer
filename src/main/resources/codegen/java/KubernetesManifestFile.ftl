---
apiVersion: v1
kind: Service
metadata:
  name: ${kubernetesPodName}
  labels:
    name: ${kubernetesPodName}-0.1.0
    app.kubernetes.io/name: ${kubernetesPodName}
spec:
  type: ClusterIP
  ports:
    - port: 8080
      targetPort: 18080
      protocol: TCP
      name: http
  selector:
    app.kubernetes.io/name: ${kubernetesPodName}
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ${kubernetesPodName}
  labels:
    name: ${kubernetesPodName}-0.1.0
    app.kubernetes.io/name: ${kubernetesPodName}
spec:
  replicas: 1
  selector:
    matchLabels:
      app.kubernetes.io/name: ${kubernetesPodName}
  template:
    metadata:
      labels:
        app.kubernetes.io/name: ${kubernetesPodName}
    spec:
      securityContext:
        {}
      containers:
        - image: ${kubernetesImage}
          name: ${kubernetesPodName}
          ports:
            - containerPort: 18080
              name: http
