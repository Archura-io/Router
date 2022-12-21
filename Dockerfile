FROM registry.access.redhat.com/ubi7/ubi-minimal

COPY io.archura.router.ArchuraRouter /
EXPOSE 8080
CMD ["/io.archura.router.ArchuraRouter"]
