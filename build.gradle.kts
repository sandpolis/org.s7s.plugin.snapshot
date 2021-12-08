//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//

plugins {
	id("java-library")
	id("com.sandpolis.build.module")
	id("com.sandpolis.build.protobuf")
	id("com.sandpolis.build.publish")
	id("com.sandpolis.build.plugin")
	id("com.sandpolis.build.codegen")
}

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.+")

	if (project.getParent() == null) {
		api("com.sandpolis:core.instance:+")
	} else {
		api(project(":core:com.sandpolis.core.instance"))
	}
}

sandpolis_plugin {
	id = project.name
	coordinate = "com.sandpolis:sandpolis-plugin-snapshot"
	name = "Snapshot Plugin"
	description = "Snapshot Plugin"
}
