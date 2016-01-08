# Sistema lance
---------------

## Sobre

Sistema de gestão de lançamentos contábeis.

## Requerimentos

- Maven
- Java
- Eclipse + Jboss tools
- Wildfly 9

## Jboss tools

Instale o Jboss tools através do Eclipse marketplace.

## Configuração do Wildfly

### Instalação do driver jdbc Oracle

- Copie o driver jdbc `ojdbc[version].jar` para este local `[wildfly folder]modules\system\layers\base\com\oracle\main` (crie se necessário).
- Dentro dessa pasta, crie o arquivo `module.xml` com o seguinte conteúdo:

```
<module xmlns="urn:jboss:module:1.1" name="com.oracle">
  <resources>
    <resource-root path="ojdbc[version].jar"/>
  </resources>
  <dependencies>
    <module name="javax.api"/>
    <module name="javax.transaction.api"/>
  </dependencies>
</module>
```

### Configuração do datasource

Adicione o servidor do WildFly em seu eclipse.
Edite o arquivo `standalone.xml`:

```
<datasources>
...
	<datasource jndi-name="java:jboss/datasources/lance" pool-name="mvSimula" enabled="true" use-java-context="true" use-ccm="true">
	    <connection-url>jdbc:oracle:thin:@10.1.0.235:1521:simula</connection-url>
	    <driver>oracle</driver>
	    <pool>
	        <min-pool-size>1</min-pool-size>
	        <max-pool-size>5</max-pool-size>
	        <prefill>true</prefill>
	    </pool>
	    <security>
	        <user-name>usrdbvah</user-name>
	        <password>vah2011</password>
	    </security>
	</datasource>
</datasources>

<drivers>
...
	<driver name="oracle" module="com.oracle">
	    <driver-class>oracle.jdbc.driver.OracleDriver</driver-class>
	</driver>
</drivers>

```

### Configuração de autenticação/autorização

No arquivo `standalone.xhtml`:

```
<security-domains>
	<security-domain name="lance" cache-type="default">
	    <authentication>
	        <login-module code="Database" flag="required">
	            <module-option name="dsJndiName" value="java:jboss/datasources/lance"/>
	            <module-option name="principalsQuery" value="select ds_senha from tb_ptc_usuario_puser where ds_login=?"/>
	            <module-option name="rolesQuery" value="select tpa.ds_perfil_acesso, 'Roles' from tb_ptc_perfil_acesso tpa, tb_ptc_perfil_acesso_usuario tpau, tb_ptc_usuario_puser tu where tpa.id_perfil_acesso = tpau.id_perfil_acesso and tu.id_puser = tpau.id_puser and tu.ds_login=?"/>
	        </login-module>
	    </authentication>
	</security-domain>
</security-domains>
```

Para base H2:

```
<security-domain name="lance" cache-type="default">
                    <authentication>
                        <login-module code="Database" flag="required">
                            <module-option name="dsJndiName" value="java:jboss/datasources/lanceH2"/>
                            <module-option name="principalsQuery" 
                            	value="select ds_senha from usrdbvah.tb_ptc_usuario_puser where ds_login=?"/>
                            <module-option name="rolesQuery" 
                            	value="select tur.cd_role, 'Roles' from usrdbvah.tb_lanca_usuario_role tur, usrdbvah.tb_ptc_usuario_puser tu where tu.id_puser = tur.id_puser and tu.ds_login=?"/>
                        </login-module>
                    </authentication>
                </security-domain>
```

## Geração de pacote

`mvn clean package`