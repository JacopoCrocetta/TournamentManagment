import { Logger } from "@nestjs/common";
import { Referees } from "src/core/entity/abstract-referees";
import { User } from "src/core/entity/abstract-user";
import { IRefereesRepository } from "src/core/repository/referees.repository";
import { IUserRepository } from "src/core/repository/user.repository";

export class UserPagePresenter{
    constructor(private repository: IUserRepository){}


    async getRefereesById(username: string, password: string): Promise<User>{
        Logger.log("RETREIVING USER WITH USERNAME " + username);
        return this.repository.getUserByUsernameAndPassword(username, password);
    }
}