# Code Template Repository Client

## Config

### Version
Check the version of client which is instelled.

```bash
repo -v
```

### Configuration file
View the configuration file

```bash
repo config
```

### Default Values
Manage default values to the settings file. Those values will be used in the next template installations.

#### Adding default values

```bash
repo config default <name> <value>

e.g.:
repo config default group com.example
repo config default artifact example
repo config default version 1.0.0
repo config default packaging jar
```

#### Removing default values

```bash
repo config remove-default <name>

e.g.:
repo config remove-default group
repo config remove-default version
```

#### Displaying default values

```bash
repo config default
```

### Dependency Aliases
Manage aliases for the dependencies you use frequently.

#### Adding dependency alias

```bash
repo config alias <alias> <dependency>

# dependecy format availables:
# -	group:artifact:version
# -	group:artifact:version:scope

e.g.: 
repo config alias junit junit:junit:4.12:test
repo config alias commons-io commons-io:commons-io:2.5 
``` 

#### Removing dependency alias

```bash
repo config remove-alias <alias>

e.g.:
repo config remove-alias junit
```

#### Displaying dependency alias

```bash
repo config alias
```

## Push a Template
Push a template to your local repository.

```bash
repo push [folder] [template-name]

# [folder] => by default = current directory
# [template-name] => by default = directory name

e.g.:
repo push
repo push template
repo push template my-template
```

## Install a Template
Install a template from your local repository.

```bash
repo install template-name [folder] [options] [dependencies]

# [folder] => by default = current directory
# [options] => options must use prefix "-", e.g.: -g my.group or -group my.group
# [dependencues] => dependencies must use prefix "+", e.g.: +junit or +commons-io:commons-io:2.5

# options:
# - b[uilder] => builder system, e.g.: maven or gradle
# - g[roup] => group
# - a[rtifact] => artifact
# - v[ersion] => version
# - packaging => packaging

e.g.:
repo install my-template
repo install my-template /home/me/workspace/
repo install my-template /home/me/workspace/ -g com.example -a example -packaging war -v 2.0
repo install my-template /home/me/workspace/ -group com.example -artifact example -packaging war -version 2.0
```

## List all templates
List all templates from local repository

```bash
repo list
```