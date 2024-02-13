import { Logger } from "@nestjs/common";
import { User } from "src/core/entity/abstract-user";
import { IUserRepository } from "src/core/repository/user.repository";

export class UserUpdatePresenter {
    constructor(private repository: IUserRepository){}

    async updateMatchById(refereesToUpdate: User):Promise<User>{
        Logger.log("TRYING TO UPDATE THE REFEREES WITH ID " + refereesToUpdate.id);

        const userUpdated = await this.repository.update(refereesToUpdate);

        Logger.log("REFEREES WITH ID " + userUpdated.id+ " IS UPDATED");
        return userUpdated;
    }
}