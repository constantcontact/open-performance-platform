# Each wpt test is split out as separate groovy files (.groovy)
# in Git repo: performance/WPT
RUN_DIR=/tmp/performance
mkdir -p $RUN_DIR
cd $RUN_DIR
REPO="git@github.com:myrepo/WPT.git"
# if directory doesn't exist, clone the repo
[ -d "WPT" ] || git clone $REPO
cd WPT
# pull latest code
git reset --hard HEAD
git clean -f
git pull