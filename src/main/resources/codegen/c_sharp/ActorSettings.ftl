{
	"plugin": {
		"name": {
			"queueMailbox": true,
			"consoleLogger": true,
			"pooledCompletes": true
		},
		"pooledCompletes": {
			"classname" : "Vlingo.Xoom.Actors.Plugin.Completes.PooledCompletesPlugin",
			"pool" : 10,
			"mailbox" : "queueMailbox"
		},
		"queueMailbox": {
			"classname": "Vlingo.Xoom.Actors.Plugin.Mailbox.ConcurrentQueue.ConcurrentQueueMailboxPlugin",
			"defaultMailbox": true,
			"numberOfDispatchersFactor": 1,
			"dispatcherThrottlingCount": 10
		},
		"consoleLogger": {
			"classname": "Vlingo.Xoom.Actors.Plugin.Logging.Console.ConsoleLoggerPlugin",
			"name": "vlingo-net/actors",
			"defaultLogger": true
		},
		"jdkLogger": {
			"handler": {
				"name": "vlingo",
				"level": "ALL"
			}
		}
	},
	"proxy": {
		"generated": {
			"classes": {
				"main": "target/classes/",
				"test": "target/test-classes/"
			},
			"sources": {
				"main": "target/generated-sources/",
				"test": "target/generated-test-sources/"
			}
		}
	}
}