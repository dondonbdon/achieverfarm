const inputField = document.getElementById('commandInput');
const mainList = document.querySelector('.main-list');

let commandList = [];
let commandListPos = commandList.length;
let logLevels = ['I', 'E'];
let emitterLive = false;
let eventSource;

window.onload = function () {
  inputField.focus();
};

inputField.addEventListener('keydown', function (event) {
  if (event.key === 'Enter' && inputField.value.trim() !== '') {
    event.preventDefault();
    inputField.disabled = true;

    const command = inputField.value.trim();
    commandList.push(command);
    commandListPos = commandList.length;

    const newItem = document.createElement('li');
    newItem.textContent = `> ${command}`;
    mainList.appendChild(newItem);

    inputField.value = '';

    resolveCommand(parse(command));

    inputField.disabled = false;
    inputField.focus();
  } else if (event.key === 'ArrowUp' && commandListPos > 0) {
    commandListPos--;
    inputField.value = commandList[commandListPos];
    moveCursorToEnd(inputField);
  } else if (event.key === 'ArrowDown' && commandListPos < commandList.length) {
    commandListPos++;
    inputField.value = commandList[commandListPos] || '';
    moveCursorToEnd(inputField);
  }
});

function moveCursorToEnd(element) {
  console.log('moving bruh');
  const length = 1;
  element.setSelectionRange(length, length);
  element.focus();
}

class Queue {
  constructor(elements = []) {
    this.items = elements;
  }

  enqueue(element) {
    this.items.push(element);
  }

  enqueueMultiple(elements) {
    elements.forEach((element) => this.enqueue(element));
  }

  dequeue() {
    if (this.isEmpty()) {
      return 'Queue is empty';
    }
    return this.items.shift();
  }

  peek() {
    if (this.isEmpty()) {
      return 'Queue is empty';
    }
    return this.items[0];
  }

  isEmpty() {
    return this.items.length === 0;
  }

  size() {
    return this.items.length;
  }
}

let parse = (s) => {
  return new Queue(s.split(' '));
};

let resolveCommand = (args) => {
  let command = args.dequeue();

  if (!emitterLive) {
    if (command === 'logs') {
      resolveLogsCommand(args);
    } else if (command === 'help') {
      resolveHelpCommand(args);
    } else if (command === 'clear') {
      resolveClearCommand();
    } else {
      printToTerminal(
        `Unrecognized command: ${command}. Type 'help' for a list of commands.`
      );
    }
  } else {
    if (command === 'quit') {
      resolveQuitCommand();
    }
  }
};

let resolveLogsCommand = (args) => {
  let options = {
    enableAllLogs: false,
    enableOrderLogs: false,
    enableInventoryLogs: false,
    enableAuthLogs: false,
    enableRealTimeLogs: false,
    start: null,
    stop: null,
    logLevel: null,
    count: 100,
    systemOnlyLogs: false,
  };

  while (!args.isEmpty()) {
    let arg0 = args.dequeue();
    if (!parseArg(arg0, args, options)) {
      return;
    }
  }

  if (!options.enableRealTimeLogs) {
    fetch(`/api/terminal/execute?command=logs`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ options: options }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        if (!data.body.length) {
          printToTerminal('No logs found.');
        } else {
          data.body.forEach((log) => {
            printToTerminal(formatLog(log));
          });
        }
      })
      .catch((error) => printToTerminal(error));
  } else {
    eventSource = new EventSource(`/api/terminal/logs/stream?enableOrderLogs=${options.enableOrderLogs}&enableInventoryLogs=${options.enableInventoryLogs}&enableAuthLogs=${options.enableInventoryLogs}&enableAllLogs=${options.enableAllLogs}`);
    printToTerminal(`real-time logs start`);
    emitterLive = true;

    eventSource.addEventListener('log', (event) => {
      console.log("recieved");
      console.log(event.data);
      printToTerminal(formatLog(JSON.parse(event.data)));
    });

    eventSource.onmessage = (event) => {
      console.log("recieved");
      console.log(event.data);
      printToTerminal(formatLog(JSON.parse(event.data)));
    };

    eventSource.onerror = (error) => {
      printToTerminal(error.message)
      printToTerminal(`real-time logs end`);
    };
  }
};

let parseArg = (arg, args, options) => {
  switch (arg) {
    case '-all':
      options.enableAllLogs = true;
      break;
    case '-a':
    case '--auth':
      options.enableAuthLogs = true;
      break;
    case '-o':
    case '--order':
      options.enableOrderLogs = true;
      break;
    case '-i':
    case '--inventory':
      options.enableInventoryLogs = true;
      break;
    case '-rt':
    case '--real-time':
      options.enableRealTimeLogs = true;
      break;
    case '-st':
    case '--start':
      options.start = new Date(args.dequeue());
      break;
    case '-so':
    case '--stop':
      options.stop = new Date(args.dequeue());
      break;
    case '-l':
    case '--level':
      options.logLevel = args.dequeue().toUpperCase();
      break;
    case '-c':
    case '--count':
      options.count = parseInt(args.dequeue(), 10);
      break;
    case '-sol':
    case '--system-only-logs':
      options.systemOnlyLogs = true;
      break;
    default:
      printToTerminal(`Unrecognized argument: ${arg}`);
      return false;
  }

  if (options.count > 500) {
    printToTerminal(`Failed: '-c' arg must be <= 500.`);
    return false;
  }

  if (options.logLevel !== null && !logLevels.includes(options.logLevel)) {
    printToTerminal(
      `Invalid log level: '${options.logLevel}'. Only 'I' and 'E' are permitted.`
    );
    return false;
  }

  return true;
};

let resolveHelpCommand = (args) => {
  if (args.isEmpty()) {
    printToTerminal(help);
  } else {
    const arg = args.dequeue();
    switch (arg) {
      case '-l':
      case '--log':
        printToTerminal(specificHelp.logs);
        break;
      default:
        printToTerminal(`Unrecognized help topic: ${arg}`);
        break;
    }
  }
};

let resolveClearCommand = () => {
  mainList.innerHTML = '';
};

let resolveQuitCommand = () => {
  emitterLive = false;
  eventSource.close();
  printToTerminal("real-time logs end");
};

function formatLog(log) {
  const typeWidth = 6;
  const dateWidth = 24;
  const authorWidth = 3;
  const levelWidth = 5;
  const nameWidth = 50;
  const noteWidth = 50;

  function padOrTruncate(str, width) {
    return str.length > width ? str.slice(0, width) : str.padEnd(width);
  }

  const date = new Date(log.timestamp).toISOString();

  const str = [
    padOrTruncate(`AF-${log.type}` || 'N/A', typeWidth),
    padOrTruncate(date, dateWidth),
    padOrTruncate(log.commanderName || 'UNKNOWN', authorWidth),
    padOrTruncate(log.level || 'I', levelWidth),
    padOrTruncate(log.name || 'NONE', nameWidth),
    padOrTruncate(log.note || '', noteWidth),
  ].join('  ');

  return str.slice(0, -1);
}

let printToTerminal = (s) => {
  s.split('\n').forEach((line) => {
    const newItem = document.createElement('li');
    newItem.textContent = line;
    mainList.appendChild(newItem);

    newItem.scrollIntoView({ behavior: 'smooth' });
  });
};

let help = `Available Commands:
- logs: Fetch logs from the system. Use 'help -l' for log-specific options.
- clear: Clear the terminal screen.
- help: Display this help message.`;

let specificHelp = {
  logs: `Log Command Options:
- -all: Enable all logs.
- -a, --auth: Enable logs for authentication.
- -o, --order: Enable logs for orders.
- -i, --inventory: Enable logs for inventory.
- -rt, --real-time: Fetch real-time logs.
- -st, --start [date]: Set the start date for log retrieval.
- -so, --stop [date]: Set the stop date for log retrieval.
- -l, --level [I|E]: Specify log level (Info or Error).
- -c, --count [number]: Number of logs to fetch (max: 500).
- -sol, --system-only-logs: Show system-only logs.`,
};
