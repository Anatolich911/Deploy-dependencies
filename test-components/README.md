# shared test components for hoopla 

This is a small set of test components that are used in almost all hoopla projects.

They are here so that we can include them in the pom and get them all baked in without doing a 
bunch of copy/paste and hoping we have the latest iterations of them.

##### git setup

If you plan on making a release, you'll nee to make sure that your git setup matches the project's.

The repository name is: `ssh://hoopla-codecommit/v1/repos/test-components`

Create a git alias for that like this:

	Host hoopla-codecommit
		Hostname      git-codecommit.us-east-1.amazonaws.com
		User          [your aws-generated id here]
		IdentityFile  ~/.ssh/id_rsa

##### DSW maven repository setup

Add this to your `~/.m2/settings.xml`:

	<!-- MWT repository -->
	<server>
		<id>midwest.release</id>
		<username>your aws access id</username>
		<password>your aws secret key</password>
	</server>
	<server>
		<id>midwest.snapshot</id>
		<username>your aws access id</username>
		<password>your aws secret key</password>
	</server>

### building a local snapshot

To test changes to these locally, all you do is this:

	mvn clean install

### Deploy a snapshot

Just run this:

	mvn clean deploy

### Snapping a real release

Run this if you're super confident:

	mvn clean gitflow:release-start gitflow:release-finish

The default prompts are correct.

If we want to use a long-lived release for testing, we can separate those like this:

	mvn clean gitflow:release-start

Test and patch as required...

	mvn clean gitflow:release-finish

Again, the default prompts are correct.

After that's built, be sure to do this:

	git merge master
	git push --all && git push --tags
