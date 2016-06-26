if [ ${PWD##*/} = crawler ]; then
    mvn compile package
    rm -rf ./out
    mkdir out
    cp target/crawler-1.0-SNAPSHOT.jar ./out
    cp -R target/lib/* ./out
else
    echo "build.sh must be run inside crawler folder"
fi