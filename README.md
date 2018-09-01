# PrimeFaces + Spring + Hibernate 整合 #

PrimeFaces + Spring + Hibernate 集合成为Web应用的好处在于它只需要尽可能少的xml配置或灵活的注解配置，另外PrimeFaces有大量开箱即用的视图组件，开发者只需将主要精力集中在业务逻辑上。

我们将PrimeFaces作为UI框架，Spring用在应用的业务层，Hibernate用作数据连接层。最后通过一个CRUD的应用运行说明整合成功。

# 0.准备要素 #

- Maven3
- MySQL5.5
- Tomcat7
- Eclipse（可选）

# 1.创建数据库和表 #

在MySQL中创建数据库psh，并执行createTable.sql

	CREATE TABLE `customer` (
	  `id` int(11) NOT NULL AUTO_INCREMENT,
	  `name` varchar(45) NOT NULL,
	  `surname` varchar(45) NOT NULL,
	  PRIMARY KEY (`id`)
	) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

# 2.利用Maven创建一个WAR工程 #

pom.xml如下

	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	  <modelVersion>4.0.0</modelVersion>
	  <groupId>com.lun</groupId>
	  <artifactId>primefacesSpring</artifactId>
	  <version>1.0.0-SNAPSHOT</version>
	  <packaging>war</packaging>
	
	  <properties>
	  	<hibernate.version>4.1.0.Final</hibernate.version>
	  	<spring.version>3.2.5.RELEASE</spring.version>
	    <failOnMissingWebXml>false</failOnMissingWebXml>
	    <maven.compiler.source>1.8</maven.compiler.source>
	    <maven.compiler.target>1.8</maven.compiler.target>
	    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	  </properties>
	  
	     <dependencies>
	
	        <!-- PrimeFaces -->
	        <dependency>
	            <groupId>org.primefaces</groupId>
	            <artifactId>primefaces</artifactId>
	            <version>6.1</version>
	        </dependency>
	
	        <!-- JSF -->
	        <dependency>
	            <groupId>com.sun.faces</groupId>
	            <artifactId>jsf-api</artifactId>
	            <version>2.1.11</version>
	        </dependency>
	        <dependency>
	            <groupId>com.sun.faces</groupId>
	            <artifactId>jsf-impl</artifactId>
	            <version>2.1.11</version>
	        </dependency>
	
	        <dependency>
	            <groupId>javax.servlet</groupId>
	            <artifactId>jstl</artifactId>
	            <version>1.2</version>
	        </dependency>
	
			<dependency>
			    <groupId>javax.servlet</groupId>
			    <artifactId>javax.servlet-api</artifactId>
			    <version>3.1.0</version>
			    <scope>provided</scope>
			</dependency>
	
	        <dependency>
	            <groupId>javax.servlet.jsp</groupId>
	            <artifactId>jsp-api</artifactId>
	            <version>2.1</version>
	            <scope>provided</scope>
	        </dependency>
	
	        <!-- EL -->
	        <dependency>
	            <groupId>org.glassfish.web</groupId>
	            <artifactId>el-impl</artifactId>
	            <version>2.2</version>
	        </dependency>
	
	        <!-- Spring 4 dependencies -->
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-core</artifactId>
	            <version>${spring.version}</version>
	        </dependency>
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-context</artifactId>
	            <version>${spring.version}</version>
	        </dependency>
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-web</artifactId>
	            <version>${spring.version}</version>
	        </dependency>
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-aop</artifactId>
	            <version>${spring.version}</version>
	        </dependency>
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-context-support</artifactId>
	            <version>${spring.version}</version>
	        </dependency>
	
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-aspects</artifactId>
	            <version>${spring.version}</version>
	        </dependency>
	
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-tx</artifactId>
	            <version>${spring.version}</version>
	        </dependency>
	
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-orm</artifactId>
	            <version>${spring.version}</version>
	        </dependency>
	
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-test</artifactId>
	            <version>${spring.version}</version>
	        </dependency>
	
	        <!-- Hibernate dependencies -->
	        <dependency>
	            <groupId>org.hibernate</groupId>
	            <artifactId>hibernate-core</artifactId>
	            <version>${hibernate.version}</version>
	        </dependency>
	        
			<!-- c3p0 dependency -->
	        <dependency>
			    <groupId>com.mchange</groupId>
			    <artifactId>c3p0</artifactId>
			    <version>0.9.5.2</version>
			</dependency>

			<dependency>
			    <groupId>mysql</groupId>
			    <artifactId>mysql-connector-java</artifactId>
			    <version>6.0.6</version>
			</dependency>
	
	    </dependencies>
	  	
	  	<build>
	  		<finalName>PSH</finalName>
	        <plugins>
	            <plugin>
					<groupId>org.apache.maven.plugins</groupId>
	                <artifactId>maven-compiler-plugin</artifactId>
	                <version>2.3.2</version>
	                <configuration>
	                    <source>1.6</source>
	                    <target>1.6</target>
	                </configuration>
	            </plugin>
	        </plugins>
	    </build>
	    
	</project>

# 3.创建WEB-INF/web.xml，配置加载Spring和JSF环境的监听器。 #

	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xmlns="http://java.sun.com/xml/ns/javaee"
	    xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
	    http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	    id="WebApp_ID" version="2.5">
	
	  <display-name>PrimeFaces Web Application</display-name>
	    <context-param>
	        <param-name>contextConfigLocation</param-name>
	        <param-value>
	            /WEB-INF/applicationContext.xml
	        </param-value>
	    </context-param>
	
	    <listener>
	        <listener-class>
	        	org.springframework.web.context.ContextLoaderListener
	        </listener-class>
	    </listener>
	    <listener>
	        <listener-class>
	            org.springframework.web.context.request.RequestContextListener
	        </listener-class>
	    </listener>
	    <!-- Change to "Production" when you are ready to deploy -->
	    <context-param>
	        <param-name>javax.faces.PROJECT_STAGE</param-name>
	        <param-value>Development</param-value>
	    </context-param>
	
	    <!-- Welcome page -->
	    <welcome-file-list>
	        <welcome-file>hello.xhtml</welcome-file>
	    </welcome-file-list>
	
	    <!-- JSF mapping -->
	    <servlet>
	        <servlet-name>Faces Servlet</servlet-name>
	        <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
	        <load-on-startup>1</load-on-startup>
	    </servlet>
	
	    <!-- Map these files with JSF -->
	    <servlet-mapping>
	        <servlet-name>Faces Servlet</servlet-name>
	        <url-pattern>/faces/*</url-pattern>
	    </servlet-mapping>
	    <servlet-mapping>
	        <servlet-name>Faces Servlet</servlet-name>
	        <url-pattern>*.jsf</url-pattern>
	    </servlet-mapping>
	    <servlet-mapping>
	        <servlet-name>Faces Servlet</servlet-name>
	        <url-pattern>*.faces</url-pattern>
	    </servlet-mapping>
	    <servlet-mapping>
	        <servlet-name>Faces Servlet</servlet-name>
	        <url-pattern>*.xhtml</url-pattern>
	    </servlet-mapping>
	    
	</web-app>

# 4.创建Spring配置文件WEB-INF/applicationContext.xml #

配置数据源,Hibernate,事务的信息配置。

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	       xmlns:tx="http://www.springframework.org/schema/tx"
	       xmlns:context="http://www.springframework.org/schema/context"
	       xsi:schemaLocation="
	        http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
	        http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context-3.0.xsd
	        http://www.springframework.org/schema/tx
	http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">
	    <context:component-scan base-package="com.lun"/>
	    <context:annotation-config/>
	    <context:spring-configured/>
	    
	    <!-- Data Source Declaration -->
	    <bean id="DataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" 
	    		destroy-method="close">
	        <property name="driverClass" value="com.mysql.cj.jdbc.Driver" />
	        <property name="jdbcUrl" value="jdbc:mysql://127.0.0.1:3306/psh?useUnicode=true&amp;characterEncoding=UTF-8&amp;serverTimezone=UTC" />
	        <property name="user" value="root" />
	        <property name="password" value="123" />
	        <property name="maxPoolSize" value="2" />
	        <property name="maxStatements" value="0" />
	        <property name="minPoolSize" value="1" />
	    </bean>
	
	    <!-- Session Factory Declaration -->
	    <bean id="SessionFactory" class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
	        <property name="dataSource" ref="DataSource" />
	        <property name="annotatedClasses">
	            <list>
	                <value>com.lun.model.Customer</value>
	            </list>
	        </property>
	        <property name="hibernateProperties">
	            <props>
	                <prop key="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</prop>
	                <prop key="hibernate.show_sql">true</prop>
	            </props>
	        </property>
	    </bean>
	
	    <!-- Enable the configuration of transactional behavior based on annotations -->
	    <tx:annotation-driven transaction-manager="txManager"/>
	
	    <!-- Transaction Manager is defined -->
	    <bean id="txManager" class="org.springframework.orm.hibernate4.HibernateTransactionManager">
	        <property name="sessionFactory" ref="SessionFactory"/>
	    </bean>
	
	</beans>

# 5.创建WEB-INF/faces-config.xml #

该配置文件主要用处主要整合Spring和Primefaces的托管Beans

	<?xml version="1.0" encoding="utf-8"?>
	<faces-config xmlns="http://java.sun.com/xml/ns/javaee"
	              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	              xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_2_0.xsd"
	              version="2.0">
	
		<application>
	        <el-resolver>org.springframework.web.jsf.el.SpringBeanFacesELResolver</el-resolver>
	    </application>
	    
	</faces-config>

# 6.创建一个领域实体和其数据连接层 #

领域模型：

	@Entity
	@Table(name="CUSTOMER")
	public class Customer implements Serializable{
	 
		private static final long serialVersionUID = 6805770934683941123L;
	
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name="ID", unique = true, nullable = false)
		private Integer id;
		
		@Column(name="NAME", unique = true, nullable = false)
	    private String name;
		
		@Column(name="SURNAME", unique = true, nullable = false)
	    private String surname;
	 
		//Omit setters and getters

	    @Override
	    public String toString() {
	        StringBuffer strBuff = new StringBuffer();
	        strBuff.append("id : ").append(getId());
	        strBuff.append(", name : ").append(getName());
	        strBuff.append(", surname : ").append(getSurname());
	        return strBuff.toString();
	    }
	}

数据连接层：

	@Repository
	public class CustomerDAO {
		
		@Autowired
		private SessionFactory sessionFactory;
	
		public SessionFactory getSessionFactory() {
			return sessionFactory;
		}
	
		public void setSessionFactory(SessionFactory sessionFactory) {
			this.sessionFactory = sessionFactory;
		}
	
		public void addCustomer(Customer customer) {
			getSessionFactory().getCurrentSession().save(customer);
		}
	
		public void deleteCustomer(Customer customer) {
			getSessionFactory().getCurrentSession().delete(customer);
		}
	
		public void updateCustomer(Customer customer) {
			getSessionFactory().getCurrentSession().update(customer);
		}
	
		@SuppressWarnings("rawtypes")
		public Customer getCustomerById(int id) {
			List list = getSessionFactory().getCurrentSession()
					.createQuery("from Customer  where id=?").setParameter(0, id).list();
			return (Customer) list.get(0);
		}
	
		@SuppressWarnings("unchecked")
		public List<Customer> getCustomers() {
			return getSessionFactory().getCurrentSession().createQuery("from Customer")
					.list();
		}
	
	}

# 7.创建main.xhtml视图文件 #

	<html xmlns="http://www.w3.org/1999/xhtml"
	      xmlns:h="http://java.sun.com/jsf/html"
	      xmlns:f="http://java.sun.com/jsf/core"
	      xmlns:ui="http://java.sun.com/jsf/facelets"
	      xmlns:p="http://primefaces.org/ui">
		
		<h:head>
			<title>The CRUD Example</title>
		</h:head>
		
		<h:body>
			<h:form>
				<p:growl id="growl" showDetail="true" sticky="true" />  
				
				<p:dataTable id="customers" 
	        			var="i" 
	        			value="#{customerMB.customerList}" 
	        			style="width: 50%">
					<p:column headerText="ID">
	                	#{i.id}
		            </p:column>
		            
		            <p:column headerText="Name">
		                #{i.name}
		            </p:column>
		            
		            <p:column headerText="Surname">
		                #{i.surname}
		            </p:column>
					
					<p:column headerText="Operation">	            	
		            	
		            	<p:commandLink value="modify"
		            				process="@this" 
		            				update="@form:display"
		            				actionListener="#{customerMB.preprocessCustomer(i)}" 
		            				oncomplete="PF('display').show()"
		            				/>
		            	#{'		'}
		            	<p:commandLink value="delete"
		            				process="@this" 
		            				update="@form:display,customers"
		            				actionListener="#{customerMB.deleteCustomer(i)}" 
		            				onclick="if(!confirm('Are You Sure?'))return false;"
		            				/>		
		            </p:column>
				
				</p:dataTable>
			    
			    <p:commandButton value="Create" 
	            				actionListener="#{customerMB.preprocessCustomer(null)}" 
	            				oncomplete="PF('display').show()"
			    				process="@this" 
			    				update="@form:display"/><!-- if you don't code like 'display' instead of '@form:display',
			    											 it will throw exception told you it can't find id component -->
			    
			    <h:link outcome="/hello.xhtml" value="Back To Home"/>
				
				<p:dialog id="display"  header="Create Or Modify" widgetVar="display" 
	          			modal="true" closable="true" resizable="false" >
					
					<h:panelGrid columns="2" cellpadding="5">
						<p:outputLabel for="name" value="name" />
						<p:inputText id="name" value="#{customerMB.customer.name}" />
						
						<p:outputLabel for="surname" value="surname" />
						<p:inputText id="surname" value="#{customerMB.customer.surname}" />
						
						<p:commandButton value="Confirm" 
									process="display"
									actionListener="#{customerMB.confirm}"
									update="display,@form:customers"
									oncomplete="if(#{customerMB.hide})PF('display').hide()"/>
						<p:commandButton value="Cancel" 
									process="@none" 
									update="@none" 
									oncomplete="PF('display').hide()"/>
					</h:panelGrid>
	
				</p:dialog>
			
			</h:form>
		</h:body>
	</html>

# 8.运行Web应用 #

将项目打包部署到Tomcat上运行,浏览器地址栏输入[http://localhost:8080/psh/crud/main.xhtml](http://localhost:8080/psh/crud/main.xhtml)

![](src/main/resources/CRUDExample.jpg)

**参考文献**

1.Primefaces + Spring + Hibernate Integration Example
[https://javabeat.net/primefaces-spring-hibernate-integration-example/](https://javabeat.net/primefaces-spring-hibernate-integration-example/)

2.PrimeFaces Showcase
[https://www.primefaces.org/showcase/](https://www.primefaces.org/showcase/)