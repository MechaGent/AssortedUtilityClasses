set /p versionDesc=Enter Version Desc String:
call git add .
call git commit -m %versionDesc%
call git push origin2 master