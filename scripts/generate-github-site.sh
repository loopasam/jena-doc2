# Dump the static site in the right place
cd asf-cms/
#From instruction on the CMS doc
export MARKDOWN_SOCKET=`pwd`/markdown.socket PYTHONPATH=`pwd`
python markdownd.py
#Generate temporary folder
perl build_site.pl --source-base=../svn/trunk/ --target-base ../
cd ../
cd content/
# Set the path right for github site
grep -rl --include="*.html" 'src="/' . | xargs sed -i 's/src=\"\//src=\"\/jena-doc2\//g'
grep -rl --include="*.html" 'href="/' . | xargs sed -i 's/href=\"\//href=\"\/jena-doc2\//g'
# Corrects the path for JS breadcrumbs
grep -rl --include="breadcrumbs.js" "var prefix = 'http://localhost/';" . | xargs sed -i 's/http:\/\/localhost\//http:\/\/loopasam.github.io\/jena-doc2\//g'
cd ..
cp -R content/* $1
# Clean the temp folder
sudo rm -R content/
