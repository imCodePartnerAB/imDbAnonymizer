<p align="center">
  <a href="https://github.com/imCodePartnerAB/imDbAnonymizer">
    <img src="https://icm.imcode.com/images/logo.gif" alt="Logo" width="432" height="70">
  </a>

  <h3 align="center">imDbAnonymizer</h3>

  <p align="center">
    Tool to anonymize data in mariadb
    <br />
    <a href="https://github.com/imCodePartnerAB/imDbAnonymizer/issues">Report bugs</a>
    ·
    <a href="https://github.com/imCodePartnerAB/imDbAnonymizer/issues">Request Feature</a>
  </p>
</p>

## Table of Content  

* [About](#about)  
* [Geting started](#geting-started)  
* [Configuration](#configuration)  
* [About me and us](#about-me-and-us)  
  
## About  
**This is an application made for KoHa. However it should not be a problem,   
using it for other programs; Primarily, because it is very customizable.**  

## Geting started  
So, first of all this is a java application, and it uses mvn to compile.  
This means that you are expected to know a little about java and mvn to  
be able to compile and use this application. You are also expected to  
understand a bit about xml to be able to configure it.  
If this is not so, dont fear; As you are able to get help [by us "imCode"](https://www.imcode.se/1095), should  
you chose to.  
  
  
Now that that is out of the way, lets get in to how to use it. First  
you will have to install the code.  

```sh
git clone https://github.com/imCodePartnerAB/imDbAnonymizer
```
  

Ok so now that this is done, we will have to compile the program.  

```sh
cd imDbAnonymizer
mvn package
```
  

You should now have a file in target/imAnonymizer-jar-with-dependencies.jar.  
This will be the file to execute.  
However if you run it now you will get an error:  

```sh
#>java -jar  target/imAnonymizer-jar-with-dependencies.jar
-- 2020/08/12 17:46:53 - Starting
-- 2020/08/12 17:46:53 - Parsing failed.  Reason: Missing required option: xmlfile
usage: imAnonymizer
    --xmlfile <file>   Use this xmlfile to run.
-- 2020/08/12 17:46:53 - java.lang.NullPointerException
--      at com.imcode.internal.imAnonymizer.parser.Value.getJdbcURl(Value.java:43)
--      at com.imcode.internal.imAnonymizer.db.connect(db.java:31)
--      at com.imcode.internal.imAnonymizer.db.<init>(db.java:23)
--      at com.imcode.internal.imAnonymizer.Main.main(Main.java:31)
--
Exception in thread "main" java.lang.NullPointerException
        at com.imcode.internal.imAnonymizer.Main.main(Main.java:33)
```
  

If you look closely you will notice that it complains about not having a  
parameter **--xmlfile**. And this is when we move to the next session  
configuration.  
  
  
## Configuration  
The configuration works by editing the XML file, and you will also use  
resource files. The resource files will contain lines that will be  
randomly chosen and inserted in the database, in the field you chose.  
  

The template/example files are located in src/non-packaged-resources  
and if we open src/non-packaged-resources/vikingfname.txt we will  
se that this contains a lot of rows with different sir names.  
  

If we now open src/non-packaged-resources/example.xml we can start  
putting  it together. First of all there is a section where you have  
to set your database you want to change. (We will not be responsible   
for any destroyed data, so make sure you do this with a test database)  
  
You can chose to just print the SQL you need to the console, and/or  
you can chose to update the database directly.  
  

The example database is preconfigured for KoHa library system, however  
we will look att the config and how you can configure it to fit your  
needs.  

Now then, the first section is <resources> this is where you configure  
the files and templates you want to call for your different tables and  
fields. You can configure <file> with <alias> you want to use further  
down and with the <file> which should contain the filename to use.  
  
You will also be able to configure <static> fields in this <resources>  
section. A <static> field will contain a <alias> you want to call  
further down and the <value> (NULL) is a valid option here if you  
want nothing in fields calling this <alias>.  
  

Now we are geting to the <tables> and subtree <table> which contains your   
configuration that maps the above <resources> to the correct table and  
field.  
  
For each <table> you will need to configure <name>, <idfield>, and also  
the subtree <fields>. The <fields> tree contains <field> which is the  
section containing each specific table field. Here you will need to set  
the <name> and <value>, where value is the alias you configure above.  
  
  
Example:  
```xml
    <tables>
        <!-- Here you specify the tables,
        <table>
            <name>issues</name>
            <idfield>issue_id</idfield>
            <fields>
                <field>
                    <name>note</name>
                    <if>notempty</if>
                    <value>ipsum</value>
                </field>
            </fields>
        </table>
    </tables>
```
  
Here you see that in the table issues, which has the idfield issue_id, we  
will insert the value of ipsum <alias> if it is notempty else it will not  
change the value from empty.  
  
 
I hope this explanation is understandable, but feel free to give me any  
pointers on how to make it more understandable.  
  
  
## About me and us  
I work for imCode AB which is a Swedish company specialized in advanced  
web-services, mostly for big organizations and municipalities services.  
  
Here I work mainly as system developer and most of my time is managing  
servers and applications running on these. From time to time I also get  
to work with system level programming and other programming to make  
life easier for customers and our other interface developers. I am far  
from a interface developer so I gladly keep in the background. Don't think  
I could make a good interface to save my life :)  
  
  
However if you contact me with a case or request we have very competent  
people both in our office in Sweden and abroad; And I would love to help  
anyone with their idea or request, and even if I'm not the man for the job  
im sure I would be able to find the right person for your case. If it is  
internet service/application related of course.  
  
  
  

