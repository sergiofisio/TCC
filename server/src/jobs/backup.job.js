const schedule = require("node-schedule");
const performBackup = require("./backupService");

module.exports = function startBackupJob() {
  schedule.scheduleJob("0 11 * * *", async () => {
    await performBackup();
  });
};
