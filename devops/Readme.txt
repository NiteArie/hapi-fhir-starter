# Run to deploy mariadb chart

helm repo add bitnami https://charts.bitnami.com/bitnami

helm install mariadb bitnami/mariadb --set auth.rootPassword=openemr --set auth.database=openemr --set auth.username=openemr --set auth.password=openemr --set primary.persistence.existingClaim=databasevolume --set volumePermissions.enabled=true