import { Inject, Injectable } from "@nestjs/common";
import { IRefereesRepository } from "src/core/repository/referees.repository";

interface CreateRefereesInput {}

@Injectable()
export class RefereesCreate {
  constructor(@Inject() refereesRepository: IRefereesRepository) {}

  create = async (input: CreateRefereesInput) => {
    return null;
  };
}
