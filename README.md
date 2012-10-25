## Description

This is a simple demonstration of how to chunk up a source XML document into smaller XML documents which need to be sent further downstream.
There is a need to do this because the target system currently imposes, among other constraints,
limits to how many records can be transmitted to it for processing at any one time.
Additionally, since no filtering or other transformation needs to take place, it seems silly to do the usual unmarshalling/marshalling
of relevant objects to accomplish this task; it's potentially slow, memory intensive, and otherwise wasteful.
Why not simply navigate the DOM?

## License

Copyright (C) 2012, pɹoɟɟǝʞ uɐp
Distributed under the Eclipse Public License.