import { PrismaClient } from "@prisma/client";
import * as dotenv from "dotenv";

dotenv.config();
const { env } = process;
const prisma = new PrismaClient();
