#!/bin/bash
#
# You can name the script starting with anonymize_ so that it is ignored by git.
#
SQLFILE='unique_sqlfile.sql'
SQLUSER=''
SQLDATABASE=''
SQLPASSWORD=''
SQLHOST=''
XMLFILE='local_kounique-koha.xml'

cd  ~/utv/git/imDbAnonymizer/src/non-packaged-resources
java -jar  ~/utv/git/imDbAnonymizer/target/imAnonymizer-jar-with-dependencies.jar -xmlfile $XMLFILE>$SQLFILE

echo "UPDATE borrower_attributes SET \`attribute\` = borrowernumber WHERE code = 'PERSNUMMER';" >> $SQLFILE
echo "UPDATE borrowers SET userid = NULL WHERE userid NOT LIKE '%imcode.com';">>$SQLFILE
echo "UPDATE borrowers SET userid = borrowernumber WHERE userid IS NULL;">>$SQLFILE
echo "UPDATE borrowers AS b SET b.cardnumber = NULL WHERE userid NOT LIKE '%imcode.com';">>$SQLFILE
echo "UPDATE borrowers AS b SET b.cardnumber = b.borrowernumber WHERE userid NOT LIKE '%imcode.com';">>$SQLFILE


cat $SQLFILE|mysql -u$SQLUSER -p$SQLPASSWORD $SQLDATABASE -h$SQLHOST
cd  ~/utv/git/imDbAnonymizer
