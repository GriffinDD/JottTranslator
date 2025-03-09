For organization and ease of import, we used a package based system and subfolders in our src directory, as shown in Phase2Hierarchy.png.

As such, all files used in our system must be added into the package system. For this submission, the full src
file has been submitted for ease of testing. To use the parse tester, place it in the src directory and add the lines:

package src;

import src.tokenizer.*;

import src.parser.*;

at the start of the file. If there are multiple tester files, this must be done for each.

As per phase 1, the parse tester files should be on the same level as src.