#      Copyright 2023 Tokuhiro Matsuno
#
#      Licensed under the Apache License, Version 2.0 (the "License");
#      you may not use this file except in compliance with the License.
#      You may obtain a copy of the License at
#
#             http://www.apache.org/licenses/LICENSE-2.0
#
#      Unless required by applicable law or agreed to in writing, software
#      distributed under the License is distributed on an "AS IS" BASIS,
#      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#      See the License for the specific language governing permissions and
#      limitations under the License.

import os
import requests
import subprocess
import tempfile


def download_file(url, filename):
    response = requests.get(url, stream=True)
    response.raise_for_status()

    with open(filename, 'wb') as file:
        for chunk in response.iter_content(chunk_size=8192):
            file.write(chunk)


# get the script directory

script_dir = os.path.dirname(os.path.realpath(__file__))
root_dir = os.path.join(script_dir, '..', '..')
print("script_dir: ", script_dir)

tmpfile = tempfile.mktemp()
outfile = os.path.join(script_dir, "heapdive-server.hprof")
heapdumptoolfile = os.path.join(script_dir, "heap-dump-tool.jar")

# remove outfile if it's exists
if os.path.exists(outfile):
    os.remove(outfile)

# Run `javac Hi.java`
subprocess.run(["./gradlew", ":heapdive-server:build"], cwd=root_dir)

# `java Hi` in background
java = subprocess.Popen(["java", "-jar", "heapdive-server/build/libs/heapdive-server-0.0.1-SNAPSHOT.jar"], cwd=root_dir)

# `jcmd <pid> GC.heap_dump $PWD/Hi.hprof` to dump heap
subprocess.run(["jcmd", str(java.pid), "GC.heap_dump", tmpfile], cwd=script_dir)

java.kill()

download_file("https://repo1.maven.org/maven2/com/paypal/heap-dump-tool/1.1.3/heap-dump-tool-1.1.3-all.jar",
              heapdumptoolfile)

subprocess.run(["java", "-jar", heapdumptoolfile, "sanitize", tmpfile, outfile], cwd=script_dir)

os.remove(tmpfile)
