#Building Chromium

## Required Tools
wget python clang llvm git lcov mercurial python3 python3-venv libnotify-bin python


##Build
As described [here](https://chromium.googlesource.com/chromium/src/+/master/docs/linux/build_instructions.md) and [here](https://chromium.googlesource.com/chromium/src/+/70.0.3538.36/docs/code_coverage.md) the following steps lead to a chromium build with coverage enabled:

'''git clone https://chromium.googlesource.com/chromium/tools/depot_tools.git
export PATH="$PATH:/home/depot_tools"
mkdir /home/chromium && cd /home/chromium
fetch --nohooks --no-history chromium
cd /home/chromium/src

./build/install-build-deps.sh
gclient runhooks 

gn gen out/coverage --args="use_clang_coverage=true is_component_build=false dcheck_always_on=true is_debug=false" '''
 

#Executing Existing URL Parser Tests
'''cd /home/chromium/src
python tools/code_coverage/coverage.py url_unittests -b out/coverage -o out/report -c 'out/coverage/url_unittests --gtest_filter=URLParser.PathURL' -f url/ '''

The generated report will be stored in '''out/report''' and contains the obtained coverage data [like this](reports/existingTestsReport/report.html).