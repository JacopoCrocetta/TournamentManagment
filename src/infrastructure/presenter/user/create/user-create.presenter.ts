import { Logger } from "@nestjs/common";
import { User } from "src/core/entity/abstract-user";
import { IUserRepository } from "src/core/repository/user.repository";
import { Md5 } from "ts-md5";

export class UserCreatePresenter {
  constructor(private repository: IUserRepository) {}

  async createMatchById(newUser: User): Promise<User> {
    Logger.log("[LOG] TRYING TO CREATE A NEW USER...");
    Logger.log("[LOG] ENCRYPTING STUFF...");
    Md5.hashAsciiStr(newUser.password.toString(), true);

    Logger.log("[LOG] CREATING AND SAVING THE USER...");
    return this.repository.create(newUser);
  }
}
