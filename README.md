Sistema Web JSF + ManagedBeans + JPA + HIBERNATE + CDI + C3P0 + Bootsfaces

JSF(JavaServer Faces) -> Front End
ManagedBeans -> Back End
JPA + HIBERNATE -> Banco de Dados

C3P0 -> Pool de conexões C3P0 integrado ao JPA: Mantém um certo número de conexões abertas com o banco de dados, por exemplo, cinco conexões abertas quando começa o sistema já, na pool(piscina): disponíveis para uso. Cada cliente pega uma conexão da pool para usar, tornando o sistema mais rapido, porque tem mais de apenas uma conexão/seção com o banco de dados

CDI(Contexts and Dependency Injection)
